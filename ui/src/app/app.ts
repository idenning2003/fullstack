import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { Toast } from 'primeng/toast';
import { HeartbeatService } from './api';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ButtonModule, Toast],
  templateUrl: './app.html',
  styleUrl: './app.scss',
  providers: [MessageService],
})
export class App implements OnInit {
  private messageService = inject(MessageService);
  private heartbeatService = inject(HeartbeatService);
  protected readonly title = signal('ui');

  ngOnInit() {
    this.heartbeatService.heartbeat().subscribe({
      next: () =>
        this.messageService.add({
          severity: 'success',
          summary: 'Success',
          detail: 'API: Connected',
        }),
      error: () =>
        this.messageService.add({
          severity: 'error',
          summary: 'Success',
          detail: 'API: Failed to connect',
        }),
    });
  }
}
