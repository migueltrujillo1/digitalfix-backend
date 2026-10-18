
#!/bin/sh
set -eu

rabbitmq-server &
server_pid=$!

trap 'kill "$server_pid" 2>/dev/null || true' TERM INT

echo "Waiting for RabbitMQ application on node2..."

attempt=0
until rabbitmq-diagnostics -q check_running; do
    attempt=$((attempt + 1))

    if [ "$attempt" -ge 90 ]; then
        echo "ERROR: RabbitMQ node2 did not become ready" >&2
        exit 1
    fi

    kill -0 "$server_pid" 2>/dev/null || exit 1
    sleep 2
done

echo "Checking cluster membership..."

# A failure to query cluster status is NOT permission to reset the node.
if ! cluster_info=$(rabbitmqctl cluster_status 2>&1); then
    echo "ERROR: Cannot read cluster status. Preserving node data." >&2
    echo "$cluster_info" >&2
    exit 1
fi

if printf '%s\n' "$cluster_info" | grep -q 'rabbit@rabbitmq-node1'; then
    echo "Node2 already belongs to the cluster. No changes needed."
else
    echo "Waiting for node1..."

    attempt=0
    until rabbitmq-diagnostics -n rabbit@rabbitmq-node1 -q check_running; do
        attempt=$((attempt + 1))

        if [ "$attempt" -ge 90 ]; then
            echo "ERROR: Node1 did not become ready" >&2
            exit 1
        fi

        sleep 2
    done

    echo "Joining node2 to node1..."
    rabbitmqctl stop_app
    rabbitmqctl join_cluster rabbit@rabbitmq-node1
    rabbitmqctl start_app
fi

echo "Node2 startup completed."
wait "$server_pid"
