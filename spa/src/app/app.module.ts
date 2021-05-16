import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './common/navigation/login/login.component';
import { RegisterComponent } from './common/navigation/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NavigationComponent } from './common/navigation/navigation.component';
import { LinksComponent } from './common/navigation/links/links.component';
import { FooterComponent } from './common/footer/footer.component';
import { MainPageComponent } from './main/main-page/main-page.component';
import { FerrytalesComponent } from './ferrytales/ferrytales/ferrytales.component';
import { PopularOffersComponent } from './main/popular-offers/popular-offers.component';
import { OfferComponent } from './main/popular-offers/offer/offer.component';
import { UsersTableComponent } from './ferrytales/users-table/users-table.component';
import { AccountDetailsComponent } from './ferrytales/account-details/account-details.component';
import { IdentityService } from './services/utils/identity.service';
import { AccessLevelTableComponent } from './ferrytales/account-details/access-level-table/access-level-table.component';
import { EditUserComponent } from './ferrytales/edit-user/edit-user.component';
import { ChangePasswordFormComponent } from './ferrytales/change-password-form/change-password-form.component';
import { AccessLevelFormComponent } from './ferrytales/access-level-form/access-level-form.component';
import { SearchFilterPipe } from './ferrytales/users-table/searchFilter.pipe';
import { ConfirmAccountComponent } from './other-views/confirm-account/confirm-account.component';
import { ResetPasswordComponent } from './common/navigation/reset-password/reset-password.component';
import { ChangeEmailFormComponent } from './ferrytales/change-email-form/change-email-form.component';
import { ConfirmEmailChangeComponent } from './other-views/confirm-email-change/confirm-email-change.component';

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
        FooterComponent,
        RegisterComponent,
        NavigationComponent,
        LinksComponent,
        MainPageComponent,
        FerrytalesComponent,
        PopularOffersComponent,
        OfferComponent,
        AccountDetailsComponent,
        AccessLevelTableComponent,
        UsersTableComponent,
        EditUserComponent,
        ChangePasswordFormComponent,
        AccessLevelFormComponent,
        SearchFilterPipe,
        ConfirmAccountComponent,
        ResetPasswordComponent,
        ChangeEmailFormComponent,
        ConfirmEmailChangeComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule
    ],
    providers: [IdentityService],
    bootstrap: [AppComponent]
})
export class AppModule {
}
