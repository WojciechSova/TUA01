import { CruiseGeneral } from './CruiseGeneral';
import { AccountGeneral } from '../mok/AccountGeneral';

export interface BookingGeneral {
    cruise: CruiseGeneral;
    account: AccountGeneral;
    number: string;
    creationDate: Date;
}
