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

db.createCollection('service_rating');

db.service_rating.insertMany([
    {
        serviceId: 1,
        clientId: 2,
        rating: 5
    }
]);