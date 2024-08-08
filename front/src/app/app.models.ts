export interface LoginForm {
  username: string;
  password: string;
}

export interface User {
  id: number;
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
  id?: number;
  name: string;
  description: string;
  price: number;
  userId: number;
}

export interface MenuItem {
  icon: string;
  text: string;
}

export type PymeMenu = Record<string, MenuItem>;

export const pymeMenu: PymeMenu = {
  'services': {
    icon: 'engineering',
    text: 'Services'
  },
  'requests': {
    icon: 'request_quote',
    text: 'Requests'
  },
  'history': {
    icon: 'history',
    text: 'History'
  }
};