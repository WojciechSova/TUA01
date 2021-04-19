import {AccountGeneral} from './AccountGeneral';

export interface AccessLevel {
    level: string;
    active: boolean;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
}
