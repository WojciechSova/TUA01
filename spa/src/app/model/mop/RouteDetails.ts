import { SeaportGeneral } from './SeaportGeneral';
import { CruiseGeneral } from "./CruiseGeneral";
import { AccountGeneral } from "../mok/AccountGeneral";

export interface RouteDetails {
    start: SeaportGeneral;
    destination: SeaportGeneral;
    cruises: CruiseGeneral[];
    code: string;
    creationDate: Date;
    createdBy: AccountGeneral;
}
