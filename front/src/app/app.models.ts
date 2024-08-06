export interface LoginForm {
  username: string;
  password: string;
}

export interface User {
  username: string;
  password: string;
  roles: string[];
  enabled: boolean;
  dni: string;
  postalCode: number;
}

export interface Client extends User {}

export interface Pyme extends User {
  pymePostalCode: string;
  pymePhone: string;
  pymeName: string;
  pymeDescription: string;
  services: Service[];
}

export interface Service {
  // Define the Service interface when the details are known
}
