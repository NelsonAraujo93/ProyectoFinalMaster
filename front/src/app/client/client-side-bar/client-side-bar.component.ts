import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { MenuItem, menuRecord } from '../../app.models';
import { ClientMenuService } from '../../services/client-menu.service';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client-side-bar',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './client-side-bar.component.html',
  styleUrls: ['./client-side-bar.component.less']
})
export class ClientSideBarComponent implements OnInit, OnDestroy {
  menu: MenuItem[] = [];
  selectedMenuItem: string = 'Pending';
  private subscription: Subscription = new Subscription();

  constructor(private menuService: ClientMenuService) {}

  ngOnInit(): void {
    this.menuService.getMenu().forEach((item: string) => {
      const menuItem: MenuItem = menuRecord[item] as MenuItem;
      this.menu.push(menuItem);
    });

    this.subscription.add(
      this.menuService.getSelectedMenuItem().subscribe(selectedItem => {
        this.selectedMenuItem = selectedItem;
      })
    );
  }

  selectMenuItem(item: string): void {
    this.menuService.setSelectedMenuItem(item);
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
