import { Component, HostListener, OnInit } from '@angular/core';

@Component({
  selector: 'app-bg-circle',
  standalone: true,
  imports: [],
  templateUrl: './bg-circle.component.html',
  styleUrl: './bg-circle.component.less'
})
export class BgCircleComponent implements OnInit {
  constructor() { }

  ngOnInit(): void {
    this.updateCircleSize();
  }

  @HostListener('window:resize', ['$event'])
  onResize(event: Event): void {
    this.updateCircleSize();
  }

  updateCircleSize(): void {
    const diameter = `${window.innerWidth}px`;
    document.documentElement.style.setProperty('--circle-diameter', diameter);
  }

}
