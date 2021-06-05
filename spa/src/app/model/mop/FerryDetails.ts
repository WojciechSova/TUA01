import { Cabin } from './Cabin';
import { AccountGeneral } from '../mok/AccountGeneral';

export interface FerryDetails {
    name: string;
    cabins: Cabin[];
    onDeckCapacity: string;
    vehicleCapacity: string;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    createdBy?: AccountGeneral;
}
