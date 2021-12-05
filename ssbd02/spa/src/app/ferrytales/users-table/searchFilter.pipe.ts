import { Pipe, PipeTransform } from '@angular/core';
import { AccountGeneral } from '../../model/mok/AccountGeneral';

@Pipe({
  name: 'searchFilter'
})
export class SearchFilterPipe implements PipeTransform {

  transform(accountsGeneral: AccountGeneral[], searchValue: string): AccountGeneral[] {
      if (!accountsGeneral || !searchValue) {
          return accountsGeneral;
      }

      return accountsGeneral.filter(account => account.firstName.toLowerCase().includes(searchValue.toLowerCase())
          || account.lastName.toLowerCase().includes(searchValue.toLowerCase()));
  }
}
