import { AccountGeneral } from '../mok/AccountGeneral';

export interface Cabin {
    capacity: string;
    cabinType: string;
    number: string;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    createdBy?: AccountGeneral;
}
