import { AccountGeneral } from '../mok/AccountGeneral';
import { VehicleType } from './VehicleType';
import { CabinGeneral } from './CabinGeneral';
import { CruiseGeneral } from './CruiseGeneral';

export interface BookingDetails {
    cruise: CruiseGeneral;
    account: AccountGeneral;
    numberOfPeople: number;
    cabin?: CabinGeneral;
    vehicleType: VehicleType;
    price: number;
    number: string;
    creationDate: Date;
}
