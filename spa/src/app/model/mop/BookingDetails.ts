import { AccountGeneral } from '../mok/AccountGeneral';
import { VehicleType } from './VehicleType';

export interface BookingDetails {
    cruise: string; // TODO change to CruiseGeneral
    account: AccountGeneral;
    numberOfPeople: number;
    cabin?: string; // TODO change to CabinGeneral
    vehicleType: VehicleType;
    price: number;
    number: string;
    creationDate: Date;
}
