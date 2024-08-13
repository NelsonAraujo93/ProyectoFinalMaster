import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ServiceRequest, RequestFinished } from '../../app.models';

@Component({
  selector: 'app-request-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './request-details.component.html',
  styleUrls: ['./request-details.component.less']
})
export class RequestDetailsComponent {
  @Input() request!: ServiceRequest | RequestFinished;
  @Output() closeRequested = new EventEmitter<void>();

  close(): void {
    this.closeRequested.emit();
  }

  isRequestFinished(request: ServiceRequest | RequestFinished): request is RequestFinished {
    return 'rating' in request && 'comment' in request && 'ratingDate' in request;
  }
}
