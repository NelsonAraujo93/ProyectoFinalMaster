import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-star-rating',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.less'
})
export class StarRatingComponent {
  @Input() rating: number = 2;
  @Output() ratingChange: EventEmitter<number> = new EventEmitter<number>();
  hoveredStar: number = 0;

  onRate(star: number): void {
    this.rating = star;
    this.ratingChange.emit(this.rating);
  }

  onMouseEnter(star: number): void {
    this.hoveredStar = star;
  }

  onMouseLeave(): void {
    this.hoveredStar = 0;
  }
}
