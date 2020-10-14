export class MenuItem {
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
};