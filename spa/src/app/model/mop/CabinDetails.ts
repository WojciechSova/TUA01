import { AccountGeneral } from '../mok/AccountGeneral';

export interface CabinDetails {
    capacity: string;
    cabinType: string;
    number: string;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    createdBy?: AccountGeneral;
}
