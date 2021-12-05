import { RouteGeneral } from './RouteGeneral';
import { FerryGeneral } from './FerryGeneral';

export interface CruiseGeneral {
    startDate: Date | string;
    endDate: Date | string;
    route?: RouteGeneral;
    ferry?: FerryGeneral;
    number: string;
    popularity?: number;
    version?: number;
}
