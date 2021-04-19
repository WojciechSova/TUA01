import {AccessLevel} from './AccessLevel';

export interface AccountGeneral {
    login: string;
    firstName: string;
    lastName: string;
    accessLevel: AccessLevel[];
}
