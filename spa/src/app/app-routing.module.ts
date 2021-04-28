import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MainPageComponent } from './main/main-page/main-page.component';
import { ExampleComponentComponent } from './ferrytales/example-component/example-component.component';
import { FerrytalesComponent } from './ferrytales/ferrytales/ferrytales.component';
import { AccountDetailsComponent } from './ferrytales/account-details/account-details.component';
import {UsersTableComponent} from './ferrytales/users-table/users-table.component';

const ferrytalesChildren: Routes = [
    { path: 'example-component', component: ExampleComponentComponent },
    { path: 'account', component: AccountDetailsComponent },
    { path: 'accounts', component: UsersTableComponent }
];


const routes: Routes = [
    { path: '', component: MainPageComponent },
    { path: 'ferrytales', component: FerrytalesComponent, children: ferrytalesChildren }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
