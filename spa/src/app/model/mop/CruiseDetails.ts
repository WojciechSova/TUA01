import { RouteGeneral } from './RouteGeneral';
import { FerryGeneral } from './FerryGeneral';
import { AccountGeneral } from '../mok/AccountGeneral';

export interface CruiseDetails {
    startDate: Date;
    endDate: Date;
    route: RouteGeneral;
    ferry: FerryGeneral;
    number: string;
    popularity?: number;
    modificationDate?: Date;
    modifiedBy?: AccountGeneral;
    creationDate: Date;
    createdBy: AccountGeneral;
    version: number;
}
