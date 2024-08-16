import { Component, Input } from '@angular/core';
import { Pyme } from '../../app.models'; // Assuming Pyme model is already defined
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-pyme-card',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  templateUrl: './pyme-card.component.html',
  styleUrls: ['./pyme-card.component.less']
})
export class PymeCardComponent {
  @Input() pyme!: Pyme;
}
