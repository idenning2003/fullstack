import { provideHttpClient } from '@angular/common/http';
import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { ToastrModule } from 'ngx-toastr';
import { Configuration } from './api';

import { routes } from './app.routes';

export function apiConfigFactory(): Configuration {
  return new Configuration({
    basePath: '/api',
  });
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    importProvidersFrom(BrowserAnimationsModule),
    importProvidersFrom(ToastrModule.forRoot()),
    provideHttpClient(),
    {
      provide: Configuration,
      useFactory: apiConfigFactory,
    },
  ],
};
