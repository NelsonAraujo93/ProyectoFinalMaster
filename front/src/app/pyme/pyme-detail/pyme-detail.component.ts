import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PublicServicesService } from '../../services/public-services.service';
import { Pyme } from '../../app.models';
import { CommonModule } from '@angular/common';
import { ServiceCardComponent } from '../../layout/service-card/service-card.component';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-pyme-detail',
  standalone: true,
  imports: [CommonModule, ServiceCardComponent, MatIconModule],
  templateUrl: './pyme-detail.component.html',
  styleUrls: ['./pyme-detail.component.less']
})
export class PymeDetailComponent implements OnInit {
  pyme: Pyme | null = null;

  constructor(
    private route: ActivatedRoute,
    private publicServicesService: PublicServicesService
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!;
    this.publicServicesService.getPymeById(id).subscribe(pyme => {
      this.pyme = pyme;
    });
  }
}
