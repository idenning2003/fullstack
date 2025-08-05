import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HeartbeatControllerService, UserControllerService } from './api';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  private toastr = inject(ToastrService);
  private heartbeatControllerService = inject(HeartbeatControllerService);
  private userControllerService = inject(UserControllerService);

  ngOnInit() {
    this.heartbeatControllerService.heartbeat().subscribe({
      next: () => this.toastr.success('API Connected'),
      error: (err) => this.toastr.error(err.error.message),
    });
  }
}
