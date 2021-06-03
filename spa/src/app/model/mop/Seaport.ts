import { AccountGeneral } from '../mok/AccountGeneral';

export interface Seaport {
    city: string;
    code: string;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    createdBy?: AccountGeneral;
}
