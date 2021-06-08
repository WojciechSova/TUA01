import { RouteGeneral } from './RouteGeneral';
import { FerryGeneral } from './FerryGeneral';

export interface CruiseGeneral {
    startDate: Date;
    endDate: Date;
    route?: RouteGeneral;
    ferry: FerryGeneral;
    number: string;
}
