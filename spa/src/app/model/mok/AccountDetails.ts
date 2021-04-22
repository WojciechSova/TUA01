import {AccessLevel} from './AccessLevel';
import {AccountGeneral} from './AccountGeneral';

export interface AccountDetails {
    login: string;
    password: string;
    active: boolean;
    confirmed: boolean;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber?: string;
    accessLevel: AccessLevel[];
    language?: string;
    timeZone?: string;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    lastKnownGoodLogin?: Date;
    lastKnownGoodLoginIp?: string;
    lastKnownBadLogin?: Date;
    lastKnownBadLoginIp?: string;
    numberOfBadLogins: number;
}
