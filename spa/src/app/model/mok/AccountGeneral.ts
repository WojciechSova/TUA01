import {AccessLevel} from './AccessLevel';

export interface AccountGeneral {
    login: string;
    active: boolean;
    firstName: string;
    lastName: string;
    accessLevel: AccessLevel[];
}
