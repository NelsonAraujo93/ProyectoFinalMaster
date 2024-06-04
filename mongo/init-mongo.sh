#!/bin/bash
set -e

echo "Creating databases and collections in MongoDB..."

mongosh <<EOF
use parking_events;
db.createCollection('parking_events');
use pollution_logs;
db.createCollection('pollution_logs');
use aggregated_data;
db.createCollection('aggregated_data');
EOF

echo "Databases and collections created successfully."
