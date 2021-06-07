import { AccountGeneral } from '../mok/AccountGeneral';
import { CabinGeneral } from './CabinGeneral';

export interface FerryDetails {
    name: string;
    cabins: CabinGeneral[];
    onDeckCapacity: string;
    vehicleCapacity: string;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    createdBy?: AccountGeneral;
}
