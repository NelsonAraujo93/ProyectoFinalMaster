import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PymeMenuService {
  menu = ['services', 'requests'];
  private selectedMenuItemSubject = new BehaviorSubject<string>('Services'); // Initialize with 'profile'
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
