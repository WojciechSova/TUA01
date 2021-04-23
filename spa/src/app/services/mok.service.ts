import {Injectable} from '@angular/core';
import {AccountGeneral} from "../model/mok/AccountGeneral";

@Injectable({
  providedIn: 'root'
})
export class MokService {

    //TODO: Zaimplementowanie metody, która korzysta z endpointa udostępnionego przez warstę logiki
    getAccounts(): AccountGeneral[] {
        const accessLevel = {
            level: "ADMIN",
            active: true,
            creationDate: new Date()
        }
        const accessLevel2 = {
            level: "CLIENT",
            active: true,
            creationDate: new Date()
        }
        const accessLevel3 = {
            level: "EMPLOYEE",
            active: false,
            creationDate: new Date()
        }
        const user = {
            login: "MojLogin",
            active: true,
            firstName: "Wojciech",
            lastName: "Sowa",
            accessLevel: [accessLevel, accessLevel2, accessLevel3]
        }
        const user2 = {
            login: "Nick",
            active: false,
            firstName: "Arciech",
            lastName: "Sodaj",
            accessLevel: [accessLevel]
        }
        return [user, user2]
    }
}
