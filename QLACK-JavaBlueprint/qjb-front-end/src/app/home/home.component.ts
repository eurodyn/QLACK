import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  constructor() {
  }

  ngOnInit() {
  }

  showDemo(demoType: string) {
    switch (demoType) {
      case 'forms1':
        break;
      case 'forms2':
        break;
      case 'forms3':
        break;
    }
  }
}
