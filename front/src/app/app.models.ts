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
  pyme: Pyme;
  averageRating?: number;
  ratingCount?: number;
  pymeId?: number;
  pymeName?: string;
  pymeDescription?: string;
  pymePostalCode?: string;
  pymePhone?: string;
}

export interface ServiceDetailed {
  service: Service;
  requests: ServiceRequest[];
}

export interface ServiceRequest {
  id?: number;
  serviceId: number;
  serviceName: string;
  clientId: number;
  status: string;
  details: string;
  requestDate: Date;
  rating?: number;
  comment?: string;
  ratingDate?: Date;
}

export interface RequestGroup {
  [key: string]: ServiceRequest[];
}

export interface MenuItem {
  icon: string;
  text: string;
}

export type Menu = Record<string, MenuItem>;

export const menuRecord: Menu = {
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
  },
  'pending': {
    icon: 'pending',
    text: 'Pending'
  },
  'on-process': {
    icon: 'hourglass_top',
    text: 'On Process'
  },
  'complete': {
    icon: 'hourglass_bottom',
    text: 'Complete'
  },
  'finalized': {
    icon: 'check',
    text: 'Finalized'
  },
  'canceled': {
    icon: 'canceled',
    text: 'Canceled'
  }
};