export class MenuItem {
  constructor(text, icon, link) {
    this.text = text
    this.icon = icon
    this.link = link
  }

  toJSON() {
    return JSON.stringify({
      text: this.text,
      icon: this.icon,
      link: this.link,
    })
  }
}
