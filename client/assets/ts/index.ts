export interface IStoreState {
  token: String,
  menu: {
    sidebar: {
      items: MenuItem[]
    }
  }
}

export interface IPOJO {
  toJSON(): string
}

export interface IMenuItem {
  getName(): string
  getIcon(): string
  getLink(): string
}

export class MenuItem implements IMenuItem, IPOJO {
  private text: string
  private icon: string
  private link: string

  constructor(text: string, icon: string, link: string) {
    this.text = text
    this.icon = icon
    this.link = link
  }

  public getName() : string {
    return this.text
  }
  public getIcon() : string {
    return this.icon
  }
  public getLink() : string {
    return this.link
  }
  public toJSON() : string {
    return JSON.stringify({
      text: this.text,
      icon: this.icon,
      link: this.link,
    })
  }
}

export enum LogoColor {
  Black = "black", 
  White = "white",
}