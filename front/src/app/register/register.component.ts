import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterClientComponent } from '../register-client/register-client.component';
import { RegisterPymeComponent } from '../register-pyme/register-pyme.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RegisterClientComponent, RegisterPymeComponent],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent {
  selectedType: 'client' | 'pyme' | null = null;

  selectType(type: 'client' | 'pyme') {
    this.selectedType = type;
  }

  goBack() {
    this.selectedType = null;
  }
}
