import { Component, inject, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HeartbeatService } from './api';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent implements OnInit {
  private toastr = inject(ToastrService);
  private heartbeatService = inject(HeartbeatService);

  ngOnInit() {
    this.heartbeatService.heartbeat().subscribe({
      next: () => this.toastr.success('API Connected'),
      error: (err) => this.toastr.error(err.error.message),
    });
  }
}
