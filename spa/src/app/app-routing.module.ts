import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './common/login/login.component';
import { RegisterComponent } from './common/register/register.component';
import { MainPageComponent } from './main/main-page/main-page.component';
import { ExampleComponentComponent } from './ferrytales/example-component/example-component.component';
import { FerrytalesComponent } from './ferrytales/ferrytales/ferrytales.component';

const mainChildren: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent }
];

const ferrytalesChildren: Routes = [
    { path: 'example-component', component: ExampleComponentComponent },
    { path: 'login', component: LoginComponent }
];


const routes: Routes = [
    { path: '', component: MainPageComponent, children: mainChildren },
    { path: 'ferrytales', component: FerrytalesComponent, children: ferrytalesChildren }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
