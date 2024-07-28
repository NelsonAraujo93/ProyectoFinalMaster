db = db.getSiblingDB('services_db');

db.createCollection('service_requests');

db.service_requests.insertMany([
    {
        serviceId: 1,
        clientId: 2,
        requestDate: new Date(),
        status: "PENDING"
    }
]);
