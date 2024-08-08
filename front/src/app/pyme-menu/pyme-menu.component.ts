import { Component, OnInit } from '@angular/core';
import { PymeMenuService } from '../services/pyme-menu.service';
import { CommonModule } from '@angular/common';
import { PymeRequestsComponent } from '../pyme-requests/pyme-requests.component';
import { PymeServicesComponent } from '../pyme-services/pyme-services.component';
import { PymeHistoryComponent } from '../pyme-history/pyme-history.component';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-pyme-menu',
  standalone: true,
  imports: [CommonModule, PymeRequestsComponent, PymeServicesComponent, PymeHistoryComponent],
  templateUrl: './pyme-menu.component.html',
  styleUrl: './pyme-menu.component.less'
})
export class PymeMenuComponent implements OnInit {
  selectedMenuItem: string = 'Services';
  private subscription: Subscription = new Subscription();

  constructor(private menuService: PymeMenuService) { }

  ngOnInit(): void {
    this.subscription.add(
      this.menuService.getSelectedMenuItem().subscribe(selectedItem => {
        this.selectedMenuItem = selectedItem;
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
