import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Service } from '../../app.models';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-service-card',
  standalone: true,
  imports: [RouterLink, MatIconModule, CommonModule],
  templateUrl: './service-card.component.html',
  styleUrl: './service-card.component.less'
})
export class ServiceCardComponent {
  @Input() service!: Service;
}
