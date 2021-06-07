import { AccountGeneral } from '../mok/AccountGeneral';
import { VehicleType } from './VehicleType';
import { CabinGeneral } from './CabinGeneral';
import { CruiseDetails } from './CruiseDetails';

export interface BookingDetails {
    cruise: CruiseDetails;
    account: AccountGeneral;
    numberOfPeople: number;
    cabin?: CabinGeneral;
    vehicleType: VehicleType;
    price: number;
    number: string;
    creationDate: Date;
}
