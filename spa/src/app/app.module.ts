import { ErrorHandler, LOCALE_ID, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './common/navigation/login/login.component';
import { RegisterComponent } from './common/navigation/register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClient, HttpClientModule } from '@angular/common/http';
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
import { NewPasswordComponent } from './other-views/new-password/new-password.component';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { LocaleService } from './services/utils/locale.service';
import '@angular/common/locales/global/pl';
import '@angular/common/locales/global/en';
import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { ConfirmResetComponent } from './ferrytales/account-details/confirm-reset/confirm-reset.component';
import { ForbiddenComponent } from './other-views/error-pages/forbidden/forbidden.component';
import { NotFoundComponent } from './other-views/error-pages/not-found/not-found.component';
import { AuthInterceptor } from './services/interceptors/auth-interceptor';
import { InternalServerErrorComponent } from './other-views/error-pages/internal-server-error/internal-server-error.component';
import { SessionTimeoutComponent } from './common/navigation/session-timeout/session-timeout.component';
import { CookieService } from 'ngx-cookie-service';
import { ErrorHandlerService } from './services/error-handlers/error-handler.service';
import { UnauthorizedComponent } from './other-views/error-pages/unauthorized/unauthorized.component';
import { SeaportsTableComponent } from './ferrytales/seaports-table/seaports-table.component';
import { SeaportDetailsComponent } from './ferrytales/seaport-details/seaport-details.component';
import { RoutesTableComponent } from './ferrytales/routes-table/routes-table.component';
import { FerriesTableComponent } from './ferrytales/ferries-table/ferries-table.component';
import { FerryDetailsComponent } from './ferrytales/ferry-details/ferry-details.component';
import { CabinTableComponent } from './ferrytales/ferry-details/cabin-table/cabin-table.component';
import { CruiseDetailsComponent } from './ferrytales/cruise-details/cruise-details.component';
import { CurrentCruisesTableComponent } from './ferrytales/current-cruises-table/current-cruises-table.component';
import { RouteDetailsComponent } from './ferrytales/route-details/route-details.component';
import { CruiseTableComponent } from './ferrytales/route-details/route-table/cruise-table.component';
import { SeaportEditComponent } from './ferrytales/seaport-details/seaport-edit/seaport-edit.component';
import { BookingDetailsComponent } from './ferrytales/booking-details/booking-details.component';
import { CabinDetailsComponent } from './ferrytales/cabin-details/cabin-details.component';
import { BookingTableComponent } from './ferrytales/booking-table/booking-table.component';
import { EditCabinComponent } from './ferrytales/edit-cabin/edit-cabin.component';
import { AddSeaportComponent } from './ferrytales/add-seaport/add-seaport.component';
import { AddCabinComponent } from './ferrytales/add-cabin/add-cabin.component';
import { AddFerryComponent } from './ferrytales/add-ferry/add-ferry.component';

export function rootLoaderFactory(http: HttpClient): any {
    return new TranslateHttpLoader(http);
}

export const httpInterceptorProviders = [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
];

export const errorHandlerProviders = [
    { provide: ErrorHandler, useClass: ErrorHandlerService },
];

export const localeServiceProviders = [
    {
        provide: LOCALE_ID,
        useFactory: (localeService: LocaleService) => localeService.getLocale(),
        deps: [LocaleService]
    },
];

@NgModule({
    declarations: [
        AppComponent,
        LoginComponent,
        RegisterComponent,
        FooterComponent,
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
        InternalServerErrorComponent,
        ConfirmResetComponent,
        NewPasswordComponent,
        ConfirmEmailChangeComponent,
        ForbiddenComponent,
        NotFoundComponent,
        SessionTimeoutComponent,
        UnauthorizedComponent,
        FerriesTableComponent,
        RoutesTableComponent,
        SeaportDetailsComponent,
        SeaportsTableComponent,
        BookingDetailsComponent,
        FerryDetailsComponent,
        CabinTableComponent,
        CruiseDetailsComponent,
        CurrentCruisesTableComponent,
        RouteDetailsComponent,
        CruiseTableComponent,
        SeaportEditComponent,
        CabinDetailsComponent,
        EditCabinComponent,
        AddSeaportComponent,
        BookingTableComponent,
        CruiseDetailsComponent,
        AddCabinComponent,
        AddFerryComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: rootLoaderFactory,
                deps: [HttpClient]
            }
        }),
    ],
    providers: [
        IdentityService,
        TranslateService,
        httpInterceptorProviders,
        errorHandlerProviders,
        localeServiceProviders,
        CookieService,
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}
