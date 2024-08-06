import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterClientComponent } from '../register-client/register-client.component';
import { RegisterPymeComponent } from '../register-pyme/register-pyme.component';
import { BgCircleComponent } from '../layout/bg-circle/bg-circle.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, RegisterClientComponent, RegisterPymeComponent, BgCircleComponent],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent {
  selectedType: 'client' | 'pyme' | null = null;
  
  selectType(type: 'client' | 'pyme') {
    this.selectedType = type;
  }

  goBack() {
    debugger;
    this.selectedType = null;
  }
}
