import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { AvatarModule } from 'primeng/avatar';
import { BadgeModule } from 'primeng/badge';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { MenuModule } from 'primeng/menu';
import { Menubar } from 'primeng/menubar';
import { Auth } from '../auth';
import { ThemeSwitcher } from '../theme-switcher/theme-switcher';

export interface ThemeState {
  preset?: string;
  primary?: string;
  surface?: string;
  darkTheme?: boolean;
}

@Component({
  selector: 'app-navbar',
  imports: [ThemeSwitcher, Menubar, AvatarModule, MenuModule, BadgeModule, InputTextModule, CommonModule, ButtonModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.scss',
})
export class Navbar implements OnInit {
  auth = inject(Auth);
  userItems: MenuItem[] | undefined;

  items: MenuItem[] = [
    {
      label: 'Home',
      icon: 'pi pi-home',
      url: '/',
    },
    {
      label: 'About',
      icon: 'pi pi-info-circle',
      url: '/about',
    },
    {
      label: 'Help',
      icon: 'pi pi-question-circle',
      url: '/help',
    },
    {
      label: 'Contact',
      icon: 'pi pi-address-book',
      url: '/contact',
    },
  ];

  ngOnInit(): void {
    const isLoggedIn = this.auth.isLoggedIn();
    this.userItems = [
      { label: 'Profile', icon: 'pi pi-user', url: '/users' },
      { label: 'Settings', icon: 'pi pi-cog', url: '/settings' },
      { separator: true },
      { label: 'Login', icon: 'pi pi-sign-in', command: () => this.login(), visible: !isLoggedIn },
      { label: 'Logout', icon: 'pi pi-sign-out', command: () => this.logout(), visible: isLoggedIn },
    ];
  }

  onThemeToggler() {
    const element = document.querySelector('html');
    if (element) {
      element.classList.toggle('my-app-dark');
    }
  }

  login() {
    this.auth.setToken('token');
    window.location.reload();
  }

  logout() {
    this.auth.removeToken();
    window.location.reload();
  }
}
