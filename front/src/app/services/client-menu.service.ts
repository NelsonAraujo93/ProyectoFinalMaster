import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ClientMenuService {
  menu = ['pending', 'on-process','complete','finished', 'canceled'];
  private selectedMenuItemSubject = new BehaviorSubject<string>('Pending'); // Initialize with 'profile'
  selectedMenuItem$: Observable<string> = this.selectedMenuItemSubject.asObservable();
  
  constructor() { }

  getMenu(): string[] {
    return this.menu;
  }

  getSelectedMenuItem(): Observable<string> {
    return this.selectedMenuItem$;
  }

  setSelectedMenuItem(item: string): void {
    this.selectedMenuItemSubject.next(item);
  }
}
