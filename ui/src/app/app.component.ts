import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { HeartbeatControllerService, UserControllerService } from './api';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  constructor(
    private toastr: ToastrService,
    private heartbeatControllerService: HeartbeatControllerService,
    private userControllerService: UserControllerService
  ) {

  }

  ngOnInit() {
    this.heartbeatControllerService.heartbeat().subscribe({
      next: res => this.toastr.success('API Connected!'),
      error: err => this.toastr.error(err.error.message)
    })
  }
}
