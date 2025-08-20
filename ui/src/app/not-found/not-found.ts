import { Component, inject, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { PanelModule } from 'primeng/panel';

@Component({
  selector: 'app-not-found',
  imports: [PanelModule],
  templateUrl: './not-found.html',
  styleUrl: './not-found.scss',
})
export class NotFound implements OnInit {
  title = inject(Title);

  ngOnInit(): void {
    this.title.setTitle('UI - 404 Not Found');
  }
}
