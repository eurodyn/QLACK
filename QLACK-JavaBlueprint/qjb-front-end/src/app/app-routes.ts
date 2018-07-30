// Configuration of Router with all available routes.
import {RouterModule} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {appConstants} from './app-constants';
import {HomeComponent} from './home/home.component';

export const routing: ModuleWithProviders = RouterModule.forRoot([
  {path: '', redirectTo: appConstants.routes.home, pathMatch: 'full'},

  {path: appConstants.routes.home, component: HomeComponent},

  {path: '**', redirectTo: appConstants.routes.home}
]);
