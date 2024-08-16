import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceRequest } from '../../app.models';

@Component({
  selector: 'app-request-card',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './request-card.component.html',
  styleUrls: ['./request-card.component.less']
})
export class RequestCardComponent {
  @Input() request!: ServiceRequest;
  @Output() detailsRequested = new EventEmitter<void>();

  viewDetails(): void {
    this.detailsRequested.emit();
  }
}
