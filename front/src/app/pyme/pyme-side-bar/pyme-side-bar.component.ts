import { Component, OnInit } from '@angular/core';
import { PymeMenuService } from '../../services/pyme-menu.service';
import { menuRecord, MenuItem } from '../../app.models';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-pyme-side-bar',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './pyme-side-bar.component.html',
  styleUrls: ['./pyme-side-bar.component.less']
})
export class PymeSideBarComponent implements OnInit {
  menu: MenuItem[] = [];
  selectedMenuItem: string = 'Services';
  private subscription: Subscription = new Subscription();

  constructor(private menuService: PymeMenuService) { }

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
